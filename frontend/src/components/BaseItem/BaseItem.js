import React from "react";

const BaseItem = ({ title, start, end }) => {
  return (
    <div class="base-item">
      <div class="title">{{ title }}</div>
      <div class="period">
        <span class="ti-calendar"></span>
        <div class="date">
          <span class="start">{{ start }}</span>
          <span class="between"></span>
          <span calss="end">{{ end }}</span>
        </div>
      </div>
    </div>
  );
};

export default BaseItem;
