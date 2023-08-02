import React from "react";
import ReactDOM from "react-dom/client";

import App from "./App";

const root = ReactDOM.createRoot(document.getElementById("app"));

// if (process.env.NODE_ENV === "development") {
//   const { worker } = require("./mock/browser");
//   worker.start();
// }

root.render(
  // <React.StrictMode>
  <App />
  // </React.StrictMode>
);
