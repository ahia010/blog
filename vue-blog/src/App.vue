<template>
  <n-config-provider :locale="locale" :date-locale="dateLocale">
    <n-message-provider>
      <router-view/>
    </n-message-provider>
  </n-config-provider>
</template>
<script setup>
import {ref} from "vue";
import { zhCN, dateZhCN } from "naive-ui";

const locale = ref(zhCN);
const dateLocale =ref(dateZhCN) ;
</script>


<style scoped>
.main{
  //min-width: 100vw;
  //height: 100vh;
}
</style>
