import React from "react";
import { MemoryRouter, Route } from "react-router-dom";
import PasswordFindResult from "./PasswordFindResult";

export default {
  title: "pages/PasswordFindResult",
  component: PasswordFindResult,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/find/result",
            state: { email: "seojihwan213@naver.com" },
          },
        ]}
      >
        <Route path="/find/result">
          <Story />
        </Route>
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <PasswordFindResult {...args} />;

export const Default = Template.bind({});

Default.args = {};
