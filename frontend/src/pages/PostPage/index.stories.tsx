import { MemoryRouter as Router, Route, Routes } from 'react-router-dom';

import PostPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/PostPage',
  component: PostPage,
} as ComponentMeta<typeof PostPage>;

const Template = () => (
  <Router initialEntries={['/posts/1']}>
    <Routes>
      <Route path="/posts/:id" element={<PostPage />}></Route>
    </Routes>
  </Router>
);

export const PostPageTemplate: ComponentStory<typeof PostPage> = Template.bind({});
PostPageTemplate.args = {};
