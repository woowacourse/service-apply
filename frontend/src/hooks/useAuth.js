import * as Api from "../api";
import { LOCAL_STORAGE_KEY } from "../constants/key";
import useTokenContext from "./useTokenContext";

const useAuth = () => {
  const { setToken } = useTokenContext();

  const login = async (payload) => {
    const { data: token } = await Api.fetchLogin(payload);

    setToken(token);
    localStorage.setItem(LOCAL_STORAGE_KEY.ACCESS_TOKEN, token);
  };

  return { login };
};

export default useAuth;
