import React from "react";
import BaseItem from "../BaseItem/BaseItem";
import Box from "../Box/Box";

import "./RecruitCard.css";

const RecruitCard = ({ title, startDateTime, endDateTime }) => {
  return (
    <Box class="recruit-card">
      <BaseItem
        title={title}
        startDateTime={startDateTime}
        endDateTime={endDateTime}
      />
    </Box>
  );
};

export default RecruitCard;
