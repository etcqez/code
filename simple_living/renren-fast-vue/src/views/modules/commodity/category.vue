<template>
  <div>
    <el-button type="danger" @click="batchDelete">批量删除</el-button>
    <el-tree :data="data" :props="defaultProps"
             ref="categoryTree"
             @node-click="handleNodeClick"
             show-checkbox
             node-key="id"
             :default-expanded-keys="expandedKey"
             :expand-on-click-node="false">
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level <= 2"
            type="text"
            size="mini"
            @click="() => append(data)">
            Append
          </el-button>
          <el-button
            v-if="node.childNodes.length == 0"
            type="text"
            size="mini"
            @click="() => remove(node, data)">
            Delete
          </el-button>
          <el-button
            type="text"
            size="mini"
            @click="() => edit(data)">
            Edit
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog title="添加/修改分类" :visible.sync="dialogVisible">
      <!--      :model="category" 动态绑定category数据对象，category在数据池中定义-->
      <el-form :model="category">
        <el-form-item label="分类名">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="category.proUnit" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="addOrUpdate">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import th from 'element-ui/src/locale/lang/th'

export default {
  data () {
    return {
      data: [],
      dialogType: '', //标识对话框类型 "add", "edit"
      dialogVisible: false, // 控制是否显示对话框
      category: {
        id: null,
        name: '',
        parentId: 0,
        catLevel: 0,
        isShow: 1,
        sort: 0,
        icon: '',
        proUnit: '',
        proCount: 0
      }, //添加的分类数据
      expandedKey: [],
      defaultProps: {
        children: 'childrenCategories',
        label: 'name'
      }
    }
  },
  methods: {
    batchDelete () {
      // alert("批量删除")
      //通过
      var checkedNodes = this.$refs.categoryTree.getCheckedNodes()
      // console.log("checkedNodes = ", checkedNodes)
      var ids = []
      var categoryName = []
      for (var i = 0; i < checkedNodes.length; i++) {
        ids.push(checkedNodes[i].id)
        categoryName.push(checkedNodes[i].name)
      }

      this.$confirm(`是否批量删除【${categoryName}】`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          // url: 'http://localhost:9090/commodity/category/delete',
          url: this.$http.adornUrl('/commodity/category/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({data}) => {
          // console.log('返回的数据data=', data)
          this.$message({
            message: '批量删除成功',
            type: 'success',
          })
          this.getCategories()
        })
      }).catch(() => {
      })
    },
    addOrUpdate () {
      if ('add' == this.dialogType) {
        // alert('添加分类')
        this.addCategory()
      }
      if ('edit' == this.dialogType) {
        // alert('修改分类')
        this.updateCategory()
      }
    },
    updateCategory () {
      var {id, name, icon, proUnit} = this.category

      this.$http({
        // url: 'http://localhost:9090/commodity/category/update',
        url: this.$http.adornUrl('/commodity/category/update'),
        method: 'post',
        data: this.$http.adornData({id, name, icon, proUnit}, false)
      }).then(({data}) => {
        // console.log('返回的数据data=', data)
        this.$message({
          message: '分类信息修改成功',
          type: 'success',
        })
        this.dialogVisible = false
        this.getCategories()
        //设置需要暂开的菜单
        this.expandedKey = [this.category.parentId]
      })
    },
    edit (data) {
      // console.log("data = ", data)
      this.dialogType = 'edit'

      this.dialogVisible = true

      //回显
      this.$http({
        // url: `http://localhost:9090/commodity/category/info/${data.id}`,
        url: this.$http.adornUrl('/commodity/category/info/${data.id}'),
        method: 'get',
      }).then(({data}) => {
        // console.log("返回的data => ", data)
        this.category.name = data.category.name
        this.category.icon = data.category.icon
        this.category.proUnit = data.category.proUnit
        //id必须绑定，因为提交入库时需要
        this.category.id = data.category.id
        //为了配合显示展开的菜单
        this.category.parentId = data.category.parentId

      })

    },
    addCategory () {
      this.$http({
        // url: 'http://localhost:9090/commodity/category/save',
        url: this.$http.adornUrl('/commodity/category/save'),
        method: 'post',
        data: this.$http.adornData(this.category, false)
      }).then(({data}) => {
        // console.log('返回的数据data=', data)
        this.$message({
          message: '分类信息保存成功',
          type: 'success',
        })
        this.dialogVisible = false
        this.getCategories()
        //设置需要暂开的菜单
        this.expandedKey = [this.category.parentId]
      })
    },
    append (data) {
      this.dialogType = 'add'
      this.dialogVisible = true

      this.category.parentId = data.id

      //添加时，重置了属性
      this.category.sort = 0
      this.category.proUnit = ''
      this.category.proCount = 0
      this.category.name = ''
      this.category.isShow = 1
      this.category.id = null
      this.category.icon = ''
      // * x 是为了将字符串转成数值
      this.category.catLevel = data.catLevel * 1 + 1

      // console.log('this.category =   ', this.category)

    },
    remove (node, data) {
      console.log('data = ', data, 'node = ', node)
      var ids = [data.id]
      this.$confirm(`是否删除【${data.name}】`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          // url: 'http://localhost:9090/commodity/category/delete ',
          url: this.$http.adornUrl('/commodity/category/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({data}) => {
          // console.log('返回的数据data=', data)
          this.$message({
            message: '操作成功',
            type: 'success',
          })
          this.getCategories()
          //设置需要暂开的菜单
          this.expandedKey = [node.parent.data.id]
        })
      }).catch(() => {
      })
    },
    handleNodeClick (data) {
      console.log(data)
    },
    getCategories () {
      this.$http({
        // url: 'http://localhost:9090/commodity/category/list/tree',
        url: this.$http.adornUrl('/commodity/category/list/tree'),
        method: 'get',
      }).then(({data}) => {
        console.log('返回的数据data=', data)
        this.data = data.data
      })
    },
  },
  created () {
    this.getCategories()
  }
}
</script>
<style scoped lang="scss">

</style>
