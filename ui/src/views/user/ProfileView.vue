<template>
  <div class="profile-page">
    <h2>个人资料</h2>
    <div class="profile-info">
      <div class="details">
        <p><strong>用户名:</strong> {{ user.id }}</p>
        <p><strong>性别:</strong> {{ user.gender }}</p>
        <p v-if="user.isAdmin"><strong>管理员</strong></p>
      </div>
    </div>
    <div v-if="isMe" class="edit-profile">
      <button @click="editPassword">修改密码</button>
      <button @click="editGender">编辑性别</button>
      <button @click="goToNewPost">发布帖子</button>
      <button @click="logout">退出登录</button>
    </div>
    <div class="posts-container">
      <p>发布的帖子：</p>
      <div v-for="post in userPosts" :key="post.id" class="post-item" @click="goToPost(post.id)">
        <h2 v-if="post.title!==''">{{ post.title }}</h2>
        <p>{{ post.content }}</p>
        <div class="post-meta">
          <span>投稿者: {{ post.author }}</span>
          <span>发布时间: {{ post.time }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import axios from "axios";
import router from "../../router";

interface User {
  id: string;
  password: string;
  isAdmin: boolean;
  gender: string;
}

interface Post {
  id: number;
  author: string;
  title: string;
  time: string;
  content: string;
}

const user = ref<User>({
  id: '',
  password: '',
  isAdmin: false,
  gender: '',
});
const userPosts = ref<Post[]>([]);
const isMe = ref<boolean>(false);

const getUser = async () => {
  const userId = router.currentRoute.value.params.id;
  if (userId === localStorage.getItem('profile')) {
    isMe.value = true;
  }
  const res = await axios.post(import.meta.env.VITE_API_URL + "/profile/getUser", {id: userId});
  if (res.status === 200) {
    user.value = res.data;
  } else {
    alert("用户信息未找到！");
  }
}

const getUserPosts = async () => {
  const res = await axios.post(import.meta.env.VITE_API_URL + "/post/userPosts", {author: user.value.id});
  userPosts.value = res.data;
}

const editPassword = () => {
  router.push({name: 'password'});
}

const editGender = () => {
  router.push({name: "gender"});
};

const goToPost = async (id: number) => {
  await router.push({name: 'post', params: {id: id}});
}

const goToNewPost = async () => {
  await router.push({name: "newPost"});
}

const logout = () => {
  localStorage.removeItem('profile');
  router.back();
}

onMounted(async () => {
  await getUser();
  await getUserPosts();
});
</script>

<style scoped>
.profile-page {
  max-width: 600px;
  margin-left: 50px;
  margin-top: 60px;
  padding: 20px;
}

.profile-info {
  display: flex;
  align-items: center;
  margin-top: 20px;
}

.avatar img {
  width: 150px;
  height: 150px;
  border-radius: 50%;
}

.details {
  margin-left: 20px;
  text-align: left;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #3aa876;
}

.posts-container {
  max-width: 800px;
  margin-top: 70px;
  padding: 20px;
}

.post-item {
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
