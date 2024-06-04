<template>
  <n-form
      inline
      label-placement="left"
      label-width="auto"
      :model="searchFormValue">
    <n-form-item label="用户名:" path="username">
      <n-input v-model:value="searchFormValue.username" placeholder="输入用户名"/>
    </n-form-item>
    <n-form-item label="邮箱:" path="email">
      <n-input v-model:value="searchFormValue.email" placeholder="输入邮箱"/>
    </n-form-item>
    <n-form-item>
      <n-button attr-type="button" @click="handleValidateClick()">
        搜索
      </n-button>
    </n-form-item>
    <n-form-item>
      <n-button attr-type="button" @click="handleValidateClick(false)">
        重置
      </n-button>
    </n-form-item>
  </n-form>
  <n-space style="margin-bottom: 10px">
    <n-button @click="save()">新增</n-button>
    <n-button @click="delUser(null)">删除</n-button>
  </n-space>
  <n-space vertical :size="12">
    <n-data-table
        ref="dataTableInst"
        :columns="columns"
        :data="data"
        :row-key="rowKey"
        @update:checked-row-keys="handleCheck"
        min-height="500"
    />
  </n-space>
  <n-pagination/>
  <n-modal
      v-model:show="showModal"
      :mask-closable="false"
      class="custom-card"
      :title="isAdd ? '新增用户' : '编辑用户'"
      preset="card"
      style="width: 600px; position: fixed; top: 100px;left: 35%">
    <n-form
        ref="formRef"
        :model="userFormValue"
        label-placement="left"
        label-width="auto">
      <n-form-item label="用户名:" path="username">
        <n-input v-model:value="userFormValue.username" placeholder="输入用户名"/>
      </n-form-item>
      <!--      <n-form-item label="头像" path="avatar">-->
      <!--        <n-upload :default-upload="false"> 点击上传</n-upload>-->
      <!--      </n-form-item>-->
      <n-form-item label="密码:" path="password">
        <n-input v-model:value="userFormValue.password" :placeholder="isAdd?'请输入密码':'未更改'"/>
      </n-form-item>
      <n-form-item label="邮箱:" path="email">
        <n-input v-model:value="userFormValue.email" placeholder="输入邮箱"/>
      </n-form-item>
      <n-form-item label="电话:" path="phone">
        <n-input v-model:value="userFormValue.phone" placeholder="输入电话"/>
      </n-form-item>
      <n-form-item>
        <n-button attr-type="button" @click="saveUser()">
          {{ isAdd ? '新增' : '保存' }}
        </n-button>
      </n-form-item>
    </n-form>
  </n-modal>

  <!--  <div>-->
  <!--    <input type="file" @change="onFileChange">-->
  <!--    <button @click="upload">Upload</button>-->
  <!--  </div>-->
</template>

<script setup>
import {h, onMounted, reactive, ref} from 'vue'
import {NButton, NImage, useMessage} from "naive-ui";
import {deleteUserRequest, getUserInfo, getUserList, saveUserRequest} from "@/utils/request.js";
import {userStore} from "@/stores/user.js";
import axios from "axios";

const user = userStore();

const isAdd = ref(false);

let userFormValue = reactive({
  id: "",
  username: "",
  password: "",
  email: "",
  phone: "",
  role: "",
  createTime: "",
  updateTime: "",

})

const showModal = ref(false);

async function save(row = null) {
  console.log(1)
  userFormValue.id = userFormValue.username = userFormValue.password = userFormValue.email = userFormValue.phone = userFormValue.role = userFormValue.createTime = userFormValue.updateTime = "";
  if (row == null) {
    isAdd.value = true;
  } else {
    console.log(row.id)
    await getUserInfo(row.id, {
      'Token': user.getUserInfo().token,
      'Content-Type': 'application/json'
    }).then(res => {
      if (res.data.code === 200) {
        userFormValue = res.data.data
      } else {
        message.error(res.data.msg);
      }
    })
  }


  showModal.value = true;
}

async function saveUser() {
  const headers = {
    'Token': user.getUserInfo().token,
    'Content-Type': 'application/json'
  }
  await saveUserRequest(userFormValue, headers).then(res => {
    console.log(res.data)
    if (res.data.code === 200) {
      message.success('保存成功')
      showModal.value = false;
      getList()
    } else {
      message.error(res.data.msg);
    }
  })
}


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
const searchFormValue = reactive({
  username: "",
  email: "",
});

function handleValidateClick(isSearch = true) {
  if (!isSearch)
    searchFormValue.username = searchFormValue.email = "";
  getList();
}


const rowKey = (row) => row.id;

function handleCheck(rowKeys) {
  checkedRowKeysRef.value = rowKeys;
}

async function delUser(param) {
  const ids = new URLSearchParams();
  if (param == null) {
    if (checkedRowKeysRef.value.length === 0) {
      message.error('请选择要删除的数据')
      return
    }
    checkedRowKeysRef.value.forEach(e => {
      ids.append('ids', e)
    })
  } else
    ids.append('ids', param)
  await deleteUserRequest(ids, {
    'Token': user.getUserInfo().token,
    'Content-Type': 'application/json'
  }).then(res => {
    if (res.data.code === 200) {
      message.success('删除成功')
      getList()
    } else {
      message.error(res.data.msg);
    }
  })
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
    render(row) {
      return h(NImage, {
        src: row.avatar,
        fit: 'cover',
        width: 50,
        height: 50,
        previewDisabled:true
      });
    }
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
    render(row) {
      return row.status === 1 ? '正常' : '已封禁'
    },
    filterOptions: [{
      label: '正常',
      value: '1'
    },
      {
        label: '已封禁',
        value: '0'
      }],
    filter(value, row) {
      return ~row.status.indexOf(value)
    }
  }, {
    title: '创建时间',
    key: 'createTime',
    sorter: {
      compare: (a, b) => a.createTime - b.createTime,
      multiple: 1
    }
  }, {
    title: '更新时间',
    key: 'updateTime',
    sorter: {
      compare: (a, b) => a.updateTime - b.updateTime,
      multiple: 2
    }
  }, {
    title: '操作',
    key: 'operation',
    render(row) {
      const edit = h(NButton, {
        type: 'primary',
        onClick: () => {
          save(row)
        }
      }, {
        default: () => '编辑'

      })
      const del = h(
          NButton, {
            type: 'error',
            style: {
              marginLeft: '10px'
            },
            onClick: () => {
              delUser(row.id)
            }
          }, {
            default: () => '删除'
          })
      return [edit, del]
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


onMounted(() => {
  // console.log(user.getUserInfo().token)
  getList()
})


async function getList() {
  const headers = {
    'Token': user.getUserInfo().token,
    'Content-Type': 'application/json'
  }
  // console.log(headers)
  const res = await getUserList({pageSize: 10, ...searchFormValue}, headers);
  if (res.data.code === 200) {
    data.value = res.data.data.records;
  } else {
    message.error(res.data.msg);
    router.push('/login')
  }
}

const checkedRowKeysRef = ref([]);


</script>
<style scoped lang="scss">

</style>