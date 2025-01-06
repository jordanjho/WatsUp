document.addEventListener('DOMContentLoaded', () => {
    const postsContainer = document.getElementById('posts');
    const postForm = document.getElementById('new-post-form');

    postForm.addEventListener('submit', (e) => {
        e.preventDefault();

        const title = document.getElementById('post-title').value;
        const content = document.getElementById('post-content').value;

        if (title && content) {
            createPost(title, content);
            postForm.reset();
        }
    });

    function createPost(title, content) {
        const post = document.createElement('div');
        post.classList.add('post');
        post.innerHTML = `
            <h3>${title}</h3>
            <p>${content}</p>
            <button class="comment-btn">Comment</button>
            <div class="comment-section"></div>
        `;
        postsContainer.prepend(post);
    
        // Store the post in localStorage
        let posts = JSON.parse(localStorage.getItem('posts')) || [];
        posts.push({ title, content });
        localStorage.setItem('posts', JSON.stringify(posts));
    }
    

    postsContainer.addEventListener('click', (e) => {
        if (e.target.classList.contains('comment-btn')) {
            const commentSection = e.target.nextElementSibling;

            if (!commentSection.querySelector('.comment-form')) {
                const commentForm = document.createElement('div');
                commentForm.classList.add('comment-form');
                commentForm.innerHTML = `
                    <input type="text" placeholder="Write a comment..." class="comment-input">
                    <button class="comment-submit-btn">Post Comment</button>
                `;
                commentSection.appendChild(commentForm);

                const commentInput = commentForm.querySelector('.comment-input');
                const submitBtn = commentForm.querySelector('.comment-submit-btn');

                commentInput.addEventListener('input', () => {
                    submitBtn.disabled = !commentInput.value.trim();
                });

                commentForm.querySelector('.comment-submit-btn').addEventListener('click', () => {
                    const commentInput = commentForm.querySelector('.comment-input');
                    const commentText = commentInput.value;

                    if (commentText.trim()) {
                        const comment = document.createElement('div');
                        comment.classList.add('comment');
                        comment.textContent = commentText;
                        commentSection.appendChild(comment);
                        commentInput.value = '';
                    }
                });
            }
        }
    });
});
