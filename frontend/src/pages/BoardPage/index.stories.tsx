import { Route, MemoryRouter as Router, Routes } from 'react-router-dom';

import BoardPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/BoardPage',
  component: BoardPage,
} as ComponentMeta<typeof BoardPage>;

const Template = () => (
  <Router initialEntries={['/board/1']}>
    <Routes>
      <Route path="/board/:id" element={<BoardPage />}></Route>
    </Routes>
  </Router>
);

export const BoardPageTemplate: ComponentStory<typeof BoardPage> = Template.bind({});
BoardPageTemplate.args = {};
