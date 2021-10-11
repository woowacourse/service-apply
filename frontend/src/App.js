import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";

import Footer from "./components/Footer/Footer";
import Header from "./components/Header/Header";
import PrivateRoute from "./components/PrivateRoute/PrivateRoute";
import ScrollToTop from "./components/ScrollToTop/ScrollToTop";

import Recruits from "./pages/Recruits/Recruits";
import Join from "./pages/Join/Join";
import ApplicationRegister from "./pages/ApplicationRegister/ApplicationRegister";
import Login from "./pages/Login/Login";
import PasswordEdit from "./pages/PasswordEdit/PasswordEdit";
import PasswordFind from "./pages/PasswordFind/PasswordFind";
import PasswordFindResult from "./pages/PasswordFindResult/PasswordFindResult";

import RecruitmentProvider from "./provider/RecruitmentProvider";
import TokenProvider from "./provider/TokenProvider";
import "./api/api";

import PATH from "./constants/path";

import "./App.css";

const App = () => {
  return (
    <TokenProvider>
      <RecruitmentProvider>
        <BrowserRouter>
          <Header />

          <main className="main">
            <ScrollToTop>
              <Switch>
                <Route path={[PATH.HOME, PATH.RECRUITS]} exact>
                  <Recruits />
                </Route>
                <Route path={PATH.NEW_APPLICATION} exact>
                  <Join />
                </Route>
                <Route path={PATH.LOGIN} exact>
                  <Login />
                </Route>
                <Route path={PATH.FIND_PASSWORD} exact>
                  <PasswordFind />
                </Route>
                <Route path={PATH.FIND_PASSWORD_RESULT} exact>
                  <PasswordFindResult />
                </Route>
                <PrivateRoute path={PATH.APPLICATION_FORM} exact>
                  <ApplicationRegister />
                </PrivateRoute>
                <PrivateRoute path={PATH.EDIT_PASSWORD} exact>
                  <PasswordEdit />
                </PrivateRoute>
              </Switch>
            </ScrollToTop>
          </main>

          <Footer />
        </BrowserRouter>
      </RecruitmentProvider>
    </TokenProvider>
  );
};

export default App;
