import { MemoryRouter } from "react-router-dom";
import PasswordFind from "./PasswordFind";

export default {
  title: "pages/PasswordFind",
  component: PasswordFind,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/find",
          },
        ]}
      >
        <Story />
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <PasswordFind {...args} />;

export const Default = Template.bind({});

Default.args = {};
