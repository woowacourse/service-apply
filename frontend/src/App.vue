<template>
  <div id="app">
    <MainHeader />
    <router-view />
    <MainFooter />
  </div>
</template>
<script>
import MainHeader from "./components/MainHeader"
import MainFooter from "./components/MainFooter"

export default {
  components: {
    MainHeader,
    MainFooter,
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

html,
body {
  margin: 0;
  padding: 0;
  height: 100%;
  background: #ced6e0;
}

#app {
  display: flex;
  height: 100%;
  flex-direction: column;
  justify-content: space-between;
}
</style>
