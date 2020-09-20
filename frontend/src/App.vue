<template>
  <div id="app">
    <router-view />
  </div>
</template>

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
body,
#app {
  height: 100%;
}

body {
  margin: 0;
}
</style>
