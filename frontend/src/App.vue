<template>
  <div id="app">
    <MainHeader />
    <router-view />
  </div>
</template>
<script>
import MainHeader from "./components/MainHeader"

export default {
  components: {
    MainHeader,
  },
}
</script>

<style>
button {
  background-color: #ffffff;
  border-radius: 10px;
  font-size: 16px;
  border: 0;
  outline: 0;
}

button:active {
  background-color: #cccccc;
}

* {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Apple SD Gothic Neo", Roboto,
    "Helvetica Neue", Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol",
    "Noto Color Emoji";
  box-sizing: border-box;
}

body {
  margin: 0;
  background: #ced6e0;
}
</style>
