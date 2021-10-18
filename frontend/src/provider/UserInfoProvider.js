import React, { useEffect, useState } from "react";
import { useHistory } from "react-router-dom";

import * as Api from "../api";
import { ERROR_MESSAGE } from "../constants/messages";
import PATH from "../constants/path";
import useTokenContext from "../hooks/useTokenContext";
import { UserInfoContext } from "../hooks/useUserInfoContext";

const UserInfoProvider = ({ children }) => {
  const [userInfo, setUserInfo] = useState(null);
  const history = useHistory();
  const { token } = useTokenContext();

  const initUserInfo = async () => {
    try {
      const { data } = await Api.fetchUserInfo({ token });

      setUserInfo(data);
    } catch (e) {
      if (e.response.status === 401) {
        alert(ERROR_MESSAGE.API.TOKEN_EXPIRED);
        history.push(PATH.LOGIN);
      } else {
        alert(ERROR_MESSAGE.API.FETCHING_USER_INFO);
      }
    }
  };

  const updateUserInfo = async (payload) => {
    await Api.fetchUserInfoEdit({ token, ...payload });
    setUserInfo((prev) => ({ ...prev, ...payload }));
  };

  useEffect(() => {
    initUserInfo();
  }, [token]);

  return (
    <UserInfoContext.Provider value={{ userInfo, updateUserInfo }}>
      {children}
    </UserInfoContext.Provider>
  );
};

export default UserInfoProvider;
