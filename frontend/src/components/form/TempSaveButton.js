import { generatePath, useHistory } from "react-router";

import { SUCCESS_MESSAGE } from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import useFormContext from "../../hooks/useFormContext";
import { generateQuery } from "../../utils/route/query";
import Button from "../@common/Button/Button";

const TempSaveButton = ({ onSaveTemp }) => {
  return (
    <Button type="button" onClick={onSaveTemp}>
      임시 저장
    </Button>
  );
};

export default TempSaveButton;
