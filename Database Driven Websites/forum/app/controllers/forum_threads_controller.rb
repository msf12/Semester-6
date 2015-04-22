class ForumThreadsController < ApplicationController
	def index
		@forumthreads = ForumThread.all
	end

	def show
		@forumthread = ForumThread.find(params[:id])
	end

	def new
		@forumthread = ForumThread.new
	end

	def create
		@forumthread = ForumThread.new(thread_params)
		@forumthread.user = current_user

		if @forumthread.save
			redirect_to @forumthread
		else
			render 'new'
		end
	end

	def edit
		@forumthread = ForumThread.find(params[:id])
	end

	def update
		@forumthread = ForumThread.find(params[:id])

		if @forumthread.update(thread_params)
			redirect_to @forumthread
		else
			render 'edit'
		end
	end

	def destroy
		@forumthread = ForumThread.find(params[:id])
		@forumthread.destroy

		redirect_to forumthreads_path
	end

	private
	def thread_params
		params.require(:forumthread).permit(:title,:initial_post)
	end
end
