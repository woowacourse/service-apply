import React from "react";
import BaseItem from "../BaseItem/BaseItem";

const CommonItem = ({ buttonLabel, activeButton, goPage, ...props }) => {
  return (
    <div class="common-item">
      <BaseItem {...props} />

      {/* button 폼의 버튼으로 교체 */}
      <button
        class="button"
        disabled={!activeButton}
        value={buttonLabel}
        onClick={goPage}
      />
    </div>
  );
};

export default CommonItem;
