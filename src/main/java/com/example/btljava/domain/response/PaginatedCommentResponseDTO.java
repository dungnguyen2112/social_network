package com.example.btljava.domain.response;

import java.util.List;

public class PaginatedCommentResponseDTO {

    private List<ResCommentDTO> comments;
    private Meta meta;

    public List<ResCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<ResCommentDTO> comments) {
        this.comments = comments;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    // Meta thông tin về phân trang
    public static class Meta {
        private int page;
        private int pageSize;
        private long totalPages;
        private long totalComments;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public long getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(long totalPages) {
            this.totalPages = totalPages;
        }

        public long getTotalComments() {
            return totalComments;
        }

        public void setTotalComments(long totalComments) {
            this.totalComments = totalComments;
        }
    }
}
