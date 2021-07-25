import React from "react";
import ReactDOM from "react-dom";
import TokenProvider from "./provider/TokenProvider";
import RecruitmentProvider from "./provider/RecruitmentProvider";
import App from "./App";

ReactDOM.render(
  <React.StrictMode>
    <TokenProvider>
      <RecruitmentProvider>
        <App />
      </RecruitmentProvider>
    </TokenProvider>
  </React.StrictMode>,
  document.getElementById("root")
);
