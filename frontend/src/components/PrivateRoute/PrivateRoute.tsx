import { Outlet, Navigate } from "react-router-dom";

import useTokenContext from "../../hooks/useTokenContext";
import { PATH } from "../../constants/path";

const PrivateRoute = () => {
  const { token } = useTokenContext();

  return token ? <Outlet /> : <Navigate to={PATH.LOGIN} />;
};

export default PrivateRoute;
