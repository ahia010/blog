<template>
  <n-space vertical :size="12">
    <n-data-table
        ref="dataTableInst"
        :columns="columns"
        :data="data"
        :row-key="rowKey"
        @update:checked-row-keys="handleCheck"
    />
  </n-space>
</template>

<script setup>
import {h, reactive, ref} from 'vue'
import {NButton} from "naive-ui";


const rowKey = (row) => row.id;

function handleCheck(rowKeys) {
  checkedRowKeysRef.value = rowKeys;
}

const columns = [
  {
    "type": "selection"
  },
  {
    title: 'id',
    key: 'id'
  },
  {
    title: '用户名',
    key: 'username',
  },
  {
    title: '头像',
    key: 'avatar',
  },
  {
    title: '电话',
    key: 'phone',
  },
  {
    title: '邮箱',
    key: 'email',
  },
  {
    title: '状态',
    key: 'status',
    filterOptions: [{
      label: '正常',
      value: '正常'
    },
      {
        label: '已封禁',
        value: '已封禁'
      }],
    filter(value, row) {
      return ~row.status.indexOf(value)
    }
  },{
    title: '创建时间',
    key: 'createTime',
    sorter: {
      compare: (a, b) => a.createTime - b.createTime,
      multiple: 1
    }
  },{
    title: '更新时间',
    key: 'updateTime',
    sorter: {
      compare: (a, b) => a.updateTime - b.updateTime,
      multiple: 2
    }
  },{
    title: '操作',
    key: 'operation',
    render(row){
      const edit = h(NButton,{
        type: 'primary',
      },{
        default: () => '编辑'

      })
      const del = h(
          NButton,{
            type: 'error',
            style: {
              marginLeft: '10px'
            }
          },{
            default: () => '删除'
          })
      return [edit,del]
    }
  }
]
const data = reactive(Array.from({length: 10}, (_, i) => ({
  id: i,
  username: '我是用户名',
  avatar: '500000000 px',
  email: '2320311566@qq.com',
  phone: '18888888888',
  status: '已封禁',
  createTime: '2024-12-12 12:12:12',
  updateTime: '2024-12-12 12:12:12',
})))
const checkedRowKeysRef = ref([]);


</script>
<style scoped lang="scss">

</style>