import { Route, MemoryRouter as Router, Routes } from 'react-router-dom';

import HashTagPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/HashTagPage',
  component: HashTagPage,
} as ComponentMeta<typeof HashTagPage>;

const Template = () => (
  <Router initialEntries={['/hashtag/조시']}>
    <Routes>
      <Route path="/hashtag/:name" element={<HashTagPage />}></Route>
    </Routes>
  </Router>
);

export const HashTagPageTemplate: ComponentStory<typeof HashTagPage> = Template.bind({});
HashTagPageTemplate.args = {};
