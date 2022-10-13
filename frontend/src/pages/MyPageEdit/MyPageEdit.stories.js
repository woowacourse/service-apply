import { MemoryRouter } from "react-router-dom";
import MyPageEdit from "./MyPageEdit";

export default {
  title: "pages/MyPageEdit",
  component: MyPageEdit,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/mypage/edit",
          },
        ]}
      >
        <Story />
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <MyPageEdit {...args} />;

export const Default = Template.bind({});

Default.args = {};
