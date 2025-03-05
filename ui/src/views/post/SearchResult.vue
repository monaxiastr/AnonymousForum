<template>
  <div class="posts-container">
    <div v-for="post in posts" :key="post.id" class="post-item" @click="goToPost(post.id)">
      <h2 v-if="post.title!==''">{{ post.title }}</h2>
      <p>{{ post.content }}</p>
      <div class="post-meta">
        <span> 投稿者: {{ post.author }} </span>
        <span> 发布时间: {{ post.time }} </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import axios from "axios";
import {onMounted, ref, watch} from "vue";
import router from "../../router";

interface Post {
  id: number;
  author: string;
  title: string;
  time: string;
  content: string;
}

const posts = ref<Post[]>([]);

const goToPost = async (id: number) => {
  await router.push({name: 'post', params: {id: id}});
}

const getPosts = async () => {
  const query = router.currentRoute.value.params.query;
  console.log(query);
  const postsRes = await axios.post(import.meta.env.VITE_API_URL + "/post/searchPosts",
      {query: query});
  posts.value = postsRes.data;
}

// 监听路由参数变化
watch(
    () => router.currentRoute.value.params.query,
    async (newQuery) => {
      if (newQuery) {
        await getPosts();
      }
    }
);

onMounted(async () => {
  await getPosts();
});
</script>

<style scoped>

.posts-container {
  justify-items: center;
  width: 100vw;
  margin-top: 70px;
  padding: 20px;
}

.post-item {
  width: 80%;
  border-bottom: 1px solid #eee;
  padding: 20px 0;

  h2 {
    margin: 0 0 10px;
  }
}

.post-item:hover {
  cursor: pointer;
  box-shadow: 0 10px 20px rgba(255, 255, 255, 0.1);
}

.post-meta {
  font-size: 0.9em;
  color: #666;

  span {
    margin-right: 10px;
  }
}
</style>