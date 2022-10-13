import { MemoryRouter } from "react-router-dom";
import MyPage from "./MyPage";

export default {
  title: "pages/MyPage",
  component: MyPage,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/mypage",
          },
        ]}
      >
        <Story />
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => <MyPage {...args} />;

export const Default = Template.bind({});
