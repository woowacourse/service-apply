import React from "react";
import PropTypes from "prop-types";

import BaseItem from "../BaseItem/BaseItem";
import Box from "../Box/Box";

import "./RecruitCard.css";

const RecruitCard = ({ title, startDateTime, endDateTime }) => {
  return (
    <Box className="recruit-card">
      <BaseItem
        title={title}
        startDateTime={startDateTime}
        endDateTime={endDateTime}
      />
    </Box>
  );
};

export default RecruitCard;

RecruitCard.propTypes = {
  title: PropTypes.string.isRequired,
  startDateTime: PropTypes.string.isRequired,
  endDateTime: PropTypes.string.isRequired,
};
