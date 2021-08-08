import React from "react";
import { Route, useHistory } from "react-router-dom";
import ApplicantRegister from "./ApplicantRegister";

export default {
  title: "page/ApplicantRegister",
  component: ApplicantRegister,
};

const ApplicantRegisterMock = () => {
  const history = useHistory();

  history.push({
    pathname: "/applicants/new",
    state: {
      recruitmentId: 3,
    },
  });

  return (
    <Route path="/applicants/new">
      <ApplicantRegister />
    </Route>
  );
};

const Template = (args) => <ApplicantRegisterMock />;

export const Default = Template.bind({});
