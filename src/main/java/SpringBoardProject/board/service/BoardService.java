package SpringBoardProject.board.service;

import SpringBoardProject.board.dto.BoardPatchDto;
import SpringBoardProject.board.dto.BoardPostDto;
import SpringBoardProject.board.dto.BoardResponseDto;
import SpringBoardProject.board.entity.Board;
import SpringBoardProject.board.exception.BusinessLogicException;
import SpringBoardProject.board.exception.ExceptionCode;
import SpringBoardProject.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Long createBoard(BoardPostDto boardPostDto) {
        Board board = new Board();
        board.setTitle(boardPostDto.getTitle());
        board.setContent(boardPostDto.getContent());

        return boardRepository.save(board).getBoardId();
    }

    public Long updateBoard(BoardPatchDto boardPatchDto, Long boardId) {
        Board board = findBoardId(boardId);
        board.setTitle(boardPatchDto.getTitle());
        board.setContent(boardPatchDto.getContent());

        return boardRepository.save(board).getBoardId();
    }

    public void deleteBoard(Long boardId) {
        findBoardId(boardId);
        boardRepository.deleteById(boardId);
    }


    public Board findBoardId(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(()->new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
    }

    public BoardResponseDto findByBoardId(Long boardId) {
        Board board = findBoardId(boardId);
        return BoardResponseDto.FindFromBoard(board);
    }

    public Page<BoardResponseDto> findAllBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        return boards.map(BoardResponseDto::FindFromBoard);
    }
}