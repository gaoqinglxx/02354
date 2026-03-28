<template>
  <div class="app-wrapper">
    <!-- 科技感背景装饰 -->
    <div class="grid-bg"></div>
    <div class="floating-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
    </div>
    
    <router-view />
  </div>
</template>

<script setup>
</script>

<style scoped>
.app-wrapper {
  min-height: 100vh;
  position: relative;
}
</style>
