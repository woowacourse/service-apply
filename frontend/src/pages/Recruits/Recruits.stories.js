import React from 'react';
import Recruits from './Recruits';
import Header from '../../components/Header/Header';
import '../../App.css';

export default {
  title: 'pages/Recruits',
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
