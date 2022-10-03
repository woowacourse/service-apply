import { MemoryRouter } from "react-router-dom";
import "../../App.css";
import Header from "../../components/Header/Header";
import Recruits from "./Recruits";

export default {
  title: "pages/Recruits",
  component: Recruits,
  decorators: [
    (Story) => (
      <MemoryRouter
        initialEntries={[
          {
            pathname: "/recruits",
          },
        ]}
      >
        <Story />
      </MemoryRouter>
    ),
  ],
};

const Template = (args) => (
  <>
    <Header />
    <main className="main">
      <Recruits {...args} />
    </main>
  </>
);

export const Default = Template.bind({});
