<template>
  <n-form
      ref="formRef"
      inline
      label-placement="left"
      label-width="auto"
      :model="formValue">
    <n-form-item label="用户名:" path="username">
      <n-input v-model:value="formValue.username" placeholder="输入用户名" />
    </n-form-item>
    <n-form-item label="邮箱:" path="email">
      <n-input v-model:value="formValue.email" placeholder="输入邮箱" />
    </n-form-item>
    <n-form-item>
      <n-button attr-type="button" @click="handleValidateClick">
        搜索
      </n-button>
    </n-form-item>
    <n-form-item>
      <n-button attr-type="button" @click="handleValidateClick">
        重置
      </n-button>
    </n-form-item>
  </n-form>
  <n-space style="margin-bottom: 10px">
    <n-button>新增</n-button>
    <n-button>删除</n-button>
  </n-space>
  <n-space vertical :size="12">
    <n-data-table
        ref="dataTableInst"
        :columns="columns"
        :data="data"
        :row-key="rowKey"
        @update:checked-row-keys="handleCheck"

    />
  </n-space>
  <n-pagination/>
  <div>
    <input type="file" @change="onFileChange">
    <button @click="upload">Upload</button>
  </div>
</template>

<script setup>
import {h, onMounted, reactive, ref} from 'vue'
import {NButton, useMessage} from "naive-ui";
import {getUserList} from "@/utils/request.js";
import axios from "axios";
import {useRouter} from "vue-router";
import {userStore} from "@/stores/user.js";

const user = userStore();


const file = ref(null);

const onFileChange = (e) => {
  file.value = e.target.files[0];
};

const upload = async () => {
  if (!file.value) {
    return;
  }

  let formData = new FormData();
  formData.append('file', file.value);

  try {
    let response = await axios.post('api/upload/img', formData);
    console.log(response.data);
  } catch (error) {
    console.error(error);
  }
};





const formRef = ref(null);
const message = useMessage();
const formValue = ref({
  title: "",
  content: "",
});
function handleValidateClick(e) {

}



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
// let data = ref(Array.from({length: 10}, (_, i) => ({
//   id: i,
//   username: '我是用户名',
//   avatar: '500000000 px',
//   email: '2320311566@qq.com',
//   phone: '18888888888',
//   status: '已封禁',
//   createTime: '2024-12-12 12:12:12',
//   updateTime: '2024-12-12 12:12:12',
// })))
// console.log(data)
let data = ref([])


onMounted(async () => {
  console.log(user.getUserInfo().token)
  const headers = {
    'Token': user.getUserInfo().token,
    'Content-Type': 'application/json'
  }
  console.log(headers)
  const res = await getUserList(null,headers);
  if (res.data.code === 200) {
    data.value = res.data.data.records;
    // console.log(data)
  } else {
    message.error(res.data.msg);
    // await router.push('/login')
  }
})
const checkedRowKeysRef = ref([]);



</script>
<style scoped lang="scss">

</style>