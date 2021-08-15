import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
import TokenProvider from "./provider/TokenProvider";
import RecruitmentProvider from "./provider/RecruitmentProvider";
import App from "./App";

ReactDOM.render(
  <React.StrictMode>
    <BrowserRouter>
      <TokenProvider>
        <RecruitmentProvider>
          <App />
        </RecruitmentProvider>
      </TokenProvider>
    </BrowserRouter>
  </React.StrictMode>,
  document.getElementById("root")
);
