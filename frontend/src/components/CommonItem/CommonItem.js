import React from "react";
import BaseItem from "../BaseItem/BaseItem";

import { Button } from "../form";

import "./CommonItem.css";

const CommonItem = ({ buttonLabel, activeButton, goPage, ...props }) => {
  return (
    <div class="common-item">
      <BaseItem {...props} />
      <Button
        class="button"
        disabled={!activeButton}
        value={buttonLabel}
        onClick={goPage}
      />
    </div>
  );
};

export default CommonItem;
