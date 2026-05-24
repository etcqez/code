<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    @close="dialogClose"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="80px">
      <el-form-item label="组名" prop="name">
        <el-input v-model="dataForm.name" placeholder="组名"></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
      </el-form-item>
      <el-form-item label="说明" prop="description">
        <el-input v-model="dataForm.description" placeholder="说明"></el-input>
      </el-form-item>
      <el-form-item label="组图标" prop="icon">
        <el-input v-model="dataForm.icon" placeholder="组图标"></el-input>
      </el-form-item>
      <el-form-item label="所属分类id" prop="categoryId">
        <!--      <el-input v-model="dataForm.categoryId" placeholder="所属分类id"></el-input>-->
        <el-cascader
          filterable
          placeholder="搜索: "
          v-model="cascadedCategoryId"
          :options="categories"
          :props="props"
        ></el-cascader>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      // cascaded  CategoryId最终提交的数据
      // categories页面展示的数据
      // props关联关系
      categories: [],
      cascadedCategoryId: [],
      props: {
        value: 'id',
        label: 'name',
        children: 'childrenCategories'
      },
      visible: false,
      dataForm: {
        id: 0,
        name: '',
        sort: '',
        description: '',
        icon: '',
        categoryId: 0
      },
      dataRule: {
        name: [
          {required: true, message: '组名不能为空', trigger: 'blur'}
        ],
        sort: [
          {required: true, message: '排序不能为空', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '说明不能为空', trigger: 'blur'}
        ],
        icon: [
          {required: true, message: '组图标不能为空', trigger: 'blur'}
        ],
        categoryId: [
          {required: true, message: '所属分类id不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  created () {
    this.getCategories()
  },
  methods: {
    dialogClose() {
      this.cascadedCategoryId = []
    },
    getCategories () {
      this.$http({
        // url: 'http://localhost:9090/commodity/category/list/tree',
        url: this.$http.adornUrl('/commodity/category/list/tree'),
        method: 'get',
      }).then(({data}) => {
        console.log('attrgroup-add-or-update返回的数据data=', data)
        this.categories = data.data
      })
    },
    //修改
    init (id) {
      this.dataForm.id = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/commodity/attrgroup/info/${this.dataForm.id}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.name = data.attrgroup.name
              this.dataForm.sort = data.attrgroup.sort
              this.dataForm.description = data.attrgroup.description
              this.dataForm.icon = data.attrgroup.icon
              this.dataForm.categoryId = data.attrgroup.categoryId
              //为了实现每次点击修改, 能够对应显示分类层级,需要获取对应的this.cascadedCategoryId
              console.log('cascadedCategoryId = ', data.attrgroup.cascadedCategoryId)
              this.cascadedCategoryId = data.attrgroup.cascadedCategoryId
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        // (3) [1, 22, 201, __ob__: Observer]
        // console.log('cascadedCategoryId = ', this.cascadedCategoryId)
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/commodity/attrgroup/${!this.dataForm.id ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'id': this.dataForm.id || undefined,
              'name': this.dataForm.name,
              'sort': this.dataForm.sort,
              'description': this.dataForm.description,
              'icon': this.dataForm.icon,
              //this.categoryId保存了用户选择的分类的层级关系
              //我们要提交的categoryId就是数组的最后元素的值
              'categoryId': this.cascadedCategoryId[this.cascadedCategoryId.length - 1]
              //更改后会报错, 校验不成功, 因为form表单的数据是空的, 解决方法: 给表单赋值一个默认值
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    }
  }
}
</script>
