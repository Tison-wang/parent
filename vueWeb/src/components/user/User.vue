<template>
  <div class="user">
    <!--<el-display></el-display>-->
    <el-row :gutter="20">
      <el-col :span="4"><el-input v-model="user.name" width="200px" placeholder="姓名" clearable	></el-input></el-col>
      <el-col :span="1"><el-button type="primary" circle @click="query">查询</el-button></el-col>
      <el-col :span="1"><el-button type="primary" circle @click="queryByName">qname</el-button></el-col>
    </el-row>
    <el-row :gutter="20">
        <el-table
          :data="tableData"
          border stripe
          size="mini"
          style="width: 50%">
          <el-table-column prop="id" label="主键" width="300px;"></el-table-column>
          <el-table-column prop="name" label="姓名"></el-table-column>
          <el-table-column header-align="center" align="center" width="250px;" >
            <template slot="header" slot-scope="scope">操作</template>
            <template slot-scope="scope">
              <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)">修改</el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <el-row :gutter="20" style="float: left;">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page=user.pageIndex
          :page-sizes="[2, 3, 5, 10, 100]"
          :page-size=user.pageSize
          layout="total, sizes, prev, pager, next, jumper"
          :total=user.total >
        </el-pagination>
      </el-row>
  </div>
</template>

<script>
  import api from "@/api";
  import { MessageBox } from 'element-ui'

    export default {
      name: "User",
      data() {
        return {
          user: {
            id: '',
            name: '',
            total: 0,
            pageIndex: 1,
            pageSize: 10
          },
          tableData: []
        }
      },
      methods: {
        query() {
            this.user.pageIndex = 1;
            this.list(this.user);
        },
        queryByName() {
            api.get({name: this.user.name})
                .then((data) => {
                    this.tableData = [data.content];
                });
        },
        list(param) {
          api.queryUserList(param)
            .then((data) => {
              this.tableData = data.content.re;
              this.user.total = data.content.total;
              this.user.pageSize = data.content.pageSize;
              this.user.pageIndex = data.content.pageIndex;
            });
        },
        handleSizeChange(val) {
          this.user.pageSize = val;
          this.list(this.user);
        },
        handleCurrentChange(val) {
          this.user.pageIndex = val;
          this.list(this.user);
        }
      },
      mounted: function() {
           this.list(this.user);
      }
    }
</script>

<style scoped>

  .el-row {
    margin-bottom: 10px;
  &:last-child {
     margin-bottom: 0;
   }
  }

</style>
