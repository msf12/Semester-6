class PostsController < ApplicationController
	def create
		@thread = ForumThread.find(params[:forum_thread_id])
		@post = @thread.posts.create(post_params)
		@post.user = current_user
		@post.save

		redirect_to forum_thread_path(@thread)
	end

	private
	def post_params
		params.require(:post).permit(:contents,:user)
	end
end
