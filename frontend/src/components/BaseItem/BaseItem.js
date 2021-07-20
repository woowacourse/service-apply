import React from "react";
import PropTypes from "prop-types";

import "./BaseItem.css";

const BaseItem = ({ title, startDateTime, endDateTime }) => {
  return (
    <div className="base-item">
      <div className="title">{title}</div>
      <div className="period">
        <span className="ti-calendar"></span>
        <div className="date">
          <span className="start">{startDateTime}</span>
          <span className="between"></span>
          <span className="end">{endDateTime}</span>
        </div>
      </div>
    </div>
  );
};

export default BaseItem;

BaseItem.propTypes = {
  title: PropTypes.string.isRequired,
  startDateTime: PropTypes.string.isRequired,
  endDateTime: PropTypes.string.isRequired,
};
