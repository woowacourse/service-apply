import { Redirect, Route } from "react-router-dom";
import useTokenContext from "../../hooks/useTokenContext";

const PrivateRoute = ({ children, ...props }) => {
  const { token } = useTokenContext();

  if (!token) alert("로그인이 필요합니다.");

  return (
    <Route {...props}>{token ? children : <Redirect to="/login" />}</Route>
  );
};

export default PrivateRoute;
