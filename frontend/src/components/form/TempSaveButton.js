import { generatePath, useHistory } from "react-router";

import { SUCCESS_MESSAGE } from "../../constants/messages";
import PATH, { PARAM } from "../../constants/path";
import useFormContext from "../../hooks/useFormContext";
import { generateQuery } from "../../utils/route/query";
import Button from "../@common/Button/Button";

const TempSaveButton = ({
  recruitmentId,
  recruitmentItems,
  status,
  onSave,
}) => {
  const history = useHistory();
  const { value } = useFormContext();

  const answers = recruitmentItems.map((item, index) => ({
    contents: value[`recruitment-item-${index}`],
    recruitmentItemId: item.id,
  }));

  const onSaveTemp = async () => {
    try {
      await onSave(answers, value.url, false);
      alert(SUCCESS_MESSAGE.API.SAVE_APPLICATION);

      if (status !== PARAM.APPLICATION_FORM_STATUS.EDIT) {
        history.replace(
          `${generatePath(PATH.APPLICATION_FORM, {
            status: PARAM.APPLICATION_FORM_STATUS.EDIT,
          })}${generateQuery({ recruitmentId })}`
        );
      }
    } catch (e) {
      alert(e.response.data.message);
      history.replace(PATH.HOME);
    }
  };

  return (
    <Button type="button" onClick={onSaveTemp}>
      임시 저장
    </Button>
  );
};

export default TempSaveButton;
