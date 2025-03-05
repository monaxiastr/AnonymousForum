<template>
  <div class="post-page">
    <div class="post-info">
      <h2>{{ post.title }}</h2>
      <div class="meta">
        <strong>投稿者:</strong> <span @click="goToProfile"> {{ post.author }} </span>
        <strong>发布时间:</strong>{{ post.time }}
      </div>
      <p>{{ post.content }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import axios from "axios";
import router from "../../router";


interface Post {
  id: number;
  author: string;
  title: string;
  time: string;
  content: string;
}

const post = ref<Post>({
  id: 0,
  author: '0',
  title: '0',
  time: '0',
  content: '0',
});

const getPost = async () => {
  console.log(router.currentRoute.value.params.id);
  const res = await axios.post(import.meta.env.VITE_API_URL + "/post/getPost",
      {id: router.currentRoute.value.params.id});
  if (res.status === 200) {
    post.value = res.data;
  } else {
    alert("帖子信息未找到！");
    router.back();
  }
};

const goToProfile = () => {
  const id = post.value.author;
  if (id !== null && id !== "") {
    router.push({name: 'profile', params: {id: id}});
  } else {
    alert("用户不存在！");
  }
};

onMounted(async () => {
  await getPost();
});

</script>

<style scoped>
.post-page {
  max-width: 600px;
  margin-left: 50px;
  margin-top: 60px;
  padding: 20px;
}

.post-info {
  align-items: center;
  margin-top: 20px;
}

.meta {
  display: inline;
  margin-left: 20px;
  text-align: left;

  span:hover {
    text-decoration: underline;
  }
}
</style>
