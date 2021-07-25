import { Redirect, Route } from "react-router-dom";

const PrivateRoute = ({ isAuthenticated, children, ...props }) => {
  if (!isAuthenticated) alert("로그인이 필요합니다.");

  return (
    <Route {...props}>
      {isAuthenticated ? children : <Redirect to="/login" />}
    </Route>
  );
};

export default PrivateRoute;
