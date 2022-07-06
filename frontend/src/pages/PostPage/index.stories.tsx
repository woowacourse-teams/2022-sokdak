import PostPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';
import { MemoryRouter as Router, Route, Routes } from 'react-router-dom';

export default {
  title: 'Pages/PostPage',
  component: PostPage,
} as ComponentMeta<typeof PostPage>;

const Template = (args: any) => (
  <Router initialEntries={['/posts/1']}>
    <Routes>
      <Route path="/posts/:id" element={<PostPage {...args} />}></Route>
    </Routes>
  </Router>
);

export const PostPageTemplate: ComponentStory<typeof PostPage> = Template.bind({});
PostPageTemplate.args = {};
