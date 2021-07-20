import React from "react";
import { Link } from "react-router-dom";

import "./MainHeader.css";

const MainHeader = () => {
  return (
    <div class="main-header">
      <header class="header">
        <nav class="nav">
          <Link to="/recruits">
            <img class="logo" src="/assets/logo/logo_full_dark.png" />
          </Link>
          <h3 class="title">
            <Link to="/recruits">지원하기</Link>
          </h3>
        </nav>
      </header>
    </div>
  );
};

export default MainHeader;
