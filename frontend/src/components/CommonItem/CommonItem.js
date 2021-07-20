import React from "react";
import PropTypes from "prop-types";

import BaseItem from "../BaseItem/BaseItem";

import { Button } from "../form";

import "./CommonItem.css";

const CommonItem = ({ buttonLabel, activeButton, recruitment, goPage }) => {
  return (
    <div className="common-item">
      <BaseItem
        title={recruitment.title}
        startDateTime={recruitment.startDateTime}
        endDateTime={recruitment.endDateTime}
      />
      <Button className="button" disabled={!activeButton} onClick={goPage}>
        {buttonLabel}
      </Button>
    </div>
  );
};

export default CommonItem;

CommonItem.propTypes = {
  buttonLabel: PropTypes.string.isRequired,
  activeButton: PropTypes.bool.isRequired,
  goPage: PropTypes.func.isRequired,
  recruitment: PropTypes.object.isRequired,
};
