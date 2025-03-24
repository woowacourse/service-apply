import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import * as Api from "../api";
import { PATH } from "../constants/path";
import { ERROR_MESSAGE } from "../constants/messages";

const useRecruitmentItem = (recruitmentId) => {
  const [recruitmentItems, setRecruitmentItems] = useState([]);
  const navigate = useNavigate();

  const init = async () => {
    try {
      const { data } = await Api.fetchRecruitmentItems(recruitmentId);

      setRecruitmentItems(data);
    } catch (error) {
      alert(ERROR_MESSAGE.API.LOAD_APPLICATION_FORM);
      navigate(PATH.HOME, { replace: true });
    }
  };

  useEffect(() => {
    init();
  }, []);

  return { recruitmentItems };
};

export default useRecruitmentItem;
