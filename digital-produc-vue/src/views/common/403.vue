<template>
  <div class="page-403">
    <svg id="robot-error" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 260 118.9" role="img">
      <title xml:lang="en">403 Error</title>
      <defs>
        <clipPath id="white-clip">
          <circle id="white-eye" fill="#cacaca" cx="130" cy="65" r="20" />
        </clipPath>
        <text id="text-s" class="error-text" y="106">403</text>
      </defs>
      <path class="alarm" fill="#e62326" d="M120.9 19.6V9.1c0-5 4.1-9.1 9.1-9.1h0c5 0 9.1 4.1 9.1 9.1v10.6" />
      <use xlink:href="#text-s" x="-0.5px" y="-1px" fill="black" />
      <use xlink:href="#text-s" fill="#2b2b2b" />
      <g id="robot">
        <g id="eye-wrap">
          <use xlink:href="#white-eye" />
          <circle
            id="eyef"
            class="eye"
            clip-path="url(#white-clip)"
            fill="#000"
            stroke="#2aa7cc"
            stroke-width="2"
            stroke-miterlimit="10"
            :cx="eyePosition.x"
            :cy="eyePosition.y"
            r="11"
          />
          <ellipse id="white-eye" fill="#2b2b2b" cx="130" cy="40" rx="18" ry="12" />
        </g>
        <circle id="tornillo" class="lightblue" cx="105" cy="32" r="2.5" />
        <use xlink:href="#tornillo" x="50" />
        <use xlink:href="#tornillo" x="50" y="60" />
        <use xlink:href="#tornillo" y="60" />
      </g>
    </svg>
    <h1>You are not allowed to enter here</h1>
    <h2>Go <a class="home-link" @click="goHome">Home!</a></h2>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores'
import { ref, onMounted, onUnmounted } from 'vue'

const router = useRouter()
const authStore = useAuthStore()

const eyePosition = ref({ x: 130, y: 65 })

const goHome = () => {
  const role = authStore.authMsg.role
  if (role === 0) {
    router.push('/manager/admin-only/home')
  } else if (role === 1) {
    router.push('/manager/digitalShop-only/home')
  } else {
    router.push('/customer/visitor-toall/home')
  }
}

const handleMouseMove = (evt) => {
  const x = evt.clientX / window.innerWidth
  const y = evt.clientY / window.innerHeight
  const newX = 115 + 30 * x
  const newY = 50 + 30 * y
  eyePosition.value = { x: newX, y: newY }
}

const handleTouchMove = (evt) => {
  if (evt.touches.length > 0) {
    const x = evt.touches[0].clientX / window.innerWidth
    const y = evt.touches[0].clientY / window.innerHeight
    const newX = 115 + 30 * x
    const newY = 50 + 30 * y
    eyePosition.value = { x: newX, y: newY }
  }
}

onMounted(() => {
  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('touchmove', handleTouchMove)
})

onUnmounted(() => {
  document.removeEventListener('mousemove', handleMouseMove)
  document.removeEventListener('touchmove', handleTouchMove)
})
</script>

<style scoped>
@font-face {
  font-family: 'Bungee';
  src: url('@/assets/fonts/Bungee-Regular.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

.page-403 {
  background: #1b1b1b;
  color: white;
  font-family: 'Bungee', cursive;
  text-align: center;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.home-link {
  color: #2aa7cc;
  text-decoration: none;
  cursor: pointer;
}
.home-link:hover {
  color: white;
}
svg {
  width: 70vw;
  max-width: 800px;
}
.lightblue {
  fill: #444;
}
#eye-wrap {
  overflow: hidden;
}
.error-text {
  font-size: 120px;
}
.alarm {
  animation: alarmOn 0.5s infinite;
}
@keyframes alarmOn {
  to {
    fill: darkred;
  }
}
h1 {
  font-size: 3rem;
}
h2 {
  font-size: 2rem;
}
</style>
