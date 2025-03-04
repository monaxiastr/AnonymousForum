<template>
  <div class="header">
    <div class="logo">
      <img alt="论坛Logo" src="../assets/banner.png" width="306" height="45" @click="goToMain">
      | Anonymous Forum
    </div>
    <div class="search-box">
      <input
          type="text"
          v-model="searchQuery"
          placeholder="搜索帖子..."
          @keyup.enter="search"
          required
      />
      <button @click="search">搜索</button>
    </div>
    <img class="avatar" alt="头像" src="../assets/banner.png" width="45" height="45" @click="goToProfile">
  </div>
  <RouterView/>
</template>

<script setup lang="ts">
import {ref} from "vue";
import router from "../router";

const searchQuery = ref('');

const goToMain = () => {
  router.push("/posts");
}

const search = () => {
  if (searchQuery.value.trim()) {
    router.push({name: 'search', params: {query: searchQuery.value}});
  } else {
    alert('请输入搜索内容');
  }
};

const goToProfile = () => {
  const id = localStorage.getItem('profile');
  if (id !== null && id !== "") {
    router.push({name: 'profile', params: {id: id}});
  } else {
    router.push("/auth");
  }
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  height: 50px;
  width: 100vw;
  background-color: dimgrey;
  position: fixed;
}

.logo {
  display: flex;
  width: 500px;
  line-height: 45px;
  text-align: center;

  img:hover {
    cursor: pointer;
  }
}

.search-box {
  display: flex;
  align-items: center;

  input {
    padding: 12px;
    border: 1px solid #ccc;
    border-radius: 4px;
    margin-right: 8px;
    flex: 1;
  }
}

/* 添加媒体查询以调整小屏幕下的样式 */
@media (max-width: 700px) {
  .header {
    align-items: flex-start; /* 使子元素靠左对齐 */
  }

  .logo {
    display: none;
  }

  .search-box {
    align-items: flex-start;
  }

}

</style>
