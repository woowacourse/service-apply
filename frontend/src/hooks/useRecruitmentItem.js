import { useEffect, useState } from "react";
import { useHistory } from "react-router";
import * as Api from "../api";
import PATH from "../constants/path";

const useRecruitmentItem = (recruitmentId) => {
  const [recruitmentItems, setRecruitmentItems] = useState([]);
  const history = useHistory();

  const init = async () => {
    try {
      const { data } = await Api.fetchItems(recruitmentId);

      setRecruitmentItems(data);
    } catch (e) {
      alert("지원서를 불러오는데 실패했습니다.");
      history.replace(PATH.HOME);
    }
  };

  useEffect(() => {
    init();
  }, []);

  return { recruitmentItems };
};

export default useRecruitmentItem;
