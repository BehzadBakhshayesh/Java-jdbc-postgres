package library.services;

import library.domain.Book;
import library.domain.Member;
import library.exceptions.LibraryException;
import library.repository.MemberRepository;

import java.util.List;

public class MemberServices {
    private final MemberRepository memberRepository;
    private Member withBorrowedBooks(Member member) {
        for (Book book : memberRepository.findBorrowedBooks(member.getId())) {
            member.addBorrowedBooks(book);
        }
        return member;
    }

    public MemberServices(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member addMember(String name, String email) {
        return memberRepository.insert(new Member(name, email));
    }

    public List<Member> allMembers() {
        return memberRepository.findAll();
    }

    public List<Member> searchMember(String text) {
        return memberRepository.search(text);
    }

    public Member findMemberById(long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new LibraryException("Member not found!"));
    }

    public Member editMember(long id, String name, String email) {
        Member.validateParams(name, email);
        findMemberById(id);
        return memberRepository.update(id, new Member(id, name, email));
    }

    public void deleteMember(long id) {
        memberRepository.delete(id);
    }

}