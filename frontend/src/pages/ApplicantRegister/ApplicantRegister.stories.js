import React from "react";
import { Route, MemoryRouter } from "react-router-dom";
import ApplicantRegister from "./ApplicantRegister";

export default {
  title: "pages/ApplicantRegister",
  component: ApplicantRegister,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/applicants/new",
            state: {
              recruitmentId: 3,
            },
          },
        ]}
      >
        <Route path="/applicants/new">
          <Story />
        </Route>
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <ApplicantRegister />;

export const Default = Template.bind({});
