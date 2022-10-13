import { MemoryRouter } from "react-router-dom";
import PasswordEdit from "./PasswordEdit";

export default {
  title: "pages/PasswordEdit",
  component: PasswordEdit,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/edit",
          },
        ]}
      >
        <Story />
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <PasswordEdit {...args} />;

export const Default = Template.bind({});

Default.args = {};
