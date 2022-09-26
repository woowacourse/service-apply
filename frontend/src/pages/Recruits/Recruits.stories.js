import "../../App.css";
import Header from "../../components/Header/Header";
import Recruits from "./Recruits";

export default {
  title: "pages/Recruits",
  component: Recruits,
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
